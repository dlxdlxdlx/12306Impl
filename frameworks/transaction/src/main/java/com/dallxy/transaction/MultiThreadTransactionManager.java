package com.dallxy.transaction;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Streams;
import lombok.Builder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MultiThreadTransactionManager {
    private final DataSourceTransactionManager dataSourceTransactionManager;
    private TransmittableThreadLocal<TransactionStatus> transactionStatus;

    public MultiThreadTransactionManager(DataSource dataSource) {
        this.dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
    }

    public void runAsync(List<Runnable> tasks, Executor executor) {
        Optional.ofNullable(executor).orElseThrow(() -> new IllegalArgumentException("Executor service can not be null"));
        AtomicBoolean ex = new AtomicBoolean(false);
        transactionStatus.set(dataSourceTransactionManager.getTransaction(null));
        List<CompletableFuture<Void>> futureList = new ArrayList<>(tasks.size());
        List<TransactionStatus> transactionStatusList = new CopyOnWriteArrayList<>();
        List<TransactionResources> transactionResources = new CopyOnWriteArrayList<>();
        try {

            tasks.forEach(task -> {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(null);
                    try {
                        transactionStatusList.add(transactionStatus);
                        transactionResources.add(TransactionResources.getThreadTransactionResource());
                        task.run();
                        dataSourceTransactionManager.commit(transactionStatus);
                    } catch (Exception e) {
                        ex.set(true);
                        futureList.forEach(f -> f.cancel(true));
                    }
                }, executor);
                futureList.add(future);
            });

            try {
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (ex.get()) {
                Streams.forEachPair(transactionStatusList.stream(), transactionResources.stream(), (status, resource) -> {
                    resource.bindTransactionResources();
                    dataSourceTransactionManager.rollback(status);
                    resource.unbindTransactionResources();
                });
            } else {
                Streams.forEachPair(transactionStatusList.stream(), transactionResources.stream(), (status, resource) -> {
                    resource.bindTransactionResources();
                    dataSourceTransactionManager.commit(status);
                    resource.unbindTransactionResources();
                });
            }
        } finally {
            transactionStatus.remove();
        }
    }

    @Builder
    public static class TransactionResources {
        private Map<Object, Object> resources = new HashMap<>();
        private Set<TransactionSynchronization> synchronizations = new HashSet<>();
        private String currentTransactionName;
        private boolean currentTransactionReadOnly;
        private int currentTransactionIsolationLevel;
        private boolean actualTransactionActive;

        public static TransactionResources getThreadTransactionResource() {
            return TransactionResources.builder()
                    .resources(TransactionSynchronizationManager.getResourceMap())
                    .synchronizations(new HashSet<>())
                    .actualTransactionActive(TransactionSynchronizationManager.isActualTransactionActive())
                    .currentTransactionName(TransactionSynchronizationManager.getCurrentTransactionName())
                    .currentTransactionReadOnly(TransactionSynchronizationManager.isCurrentTransactionReadOnly())
                    .build();
        }

        public void bindTransactionResources() {
            TransactionSynchronizationManager.bindResource(this, this);
            TransactionSynchronizationManager.setCurrentTransactionName(this.currentTransactionName);
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(this.currentTransactionReadOnly);
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(this.currentTransactionIsolationLevel);
            TransactionSynchronizationManager.setActualTransactionActive(this.actualTransactionActive);
            resources.forEach(TransactionSynchronizationManager::bindResource);
        }

        public void unbindTransactionResources() {
            resources.keySet().forEach(TransactionSynchronizationManager::unbindResource);
            TransactionSynchronizationManager.unbindResource(this);
        }

    }

}
