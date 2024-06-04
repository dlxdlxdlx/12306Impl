package com.dallxy.database.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageResponse<T> implements Serializable {
    private Long current;
    private Long size;
    private Long total;
    private List<T> records;

    public PageResponse() {
    }
    public PageResponse(Long current,Long size,Long total, List<T>record){
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = record;
    }

    public PageResponse setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public <R> PageResponse<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return ((PageResponse<R>) this).setRecords(collect);
    }


    public static <S, T> PageResponse<T> convert(Page<S> page, Function<S, T> map) {
        List<T> dataList = page.getRecords().stream()
                .map(map)
                .collect(Collectors.toList());

        return new PageResponse<T>(page.getCurrent(),page.getSize(),page.getTotal(),dataList);
    }

    public Long getCurrent() {
        return this.current;
    }

    public Long getSize() {
        return this.size;
    }

    public Long getTotal() {
        return this.total;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PageResponse)) return false;
        final PageResponse<?> other = (PageResponse<?>) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$current = this.getCurrent();
        final Object other$current = other.getCurrent();
        if (this$current == null ? other$current != null : !this$current.equals(other$current)) return false;
        final Object this$size = this.getSize();
        final Object other$size = other.getSize();
        if (this$size == null ? other$size != null : !this$size.equals(other$size)) return false;
        final Object this$total = this.getTotal();
        final Object other$total = other.getTotal();
        if (this$total == null ? other$total != null : !this$total.equals(other$total)) return false;
        final Object this$records = this.getRecords();
        final Object other$records = other.getRecords();
        if (this$records == null ? other$records != null : !this$records.equals(other$records)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageResponse;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $current = this.getCurrent();
        result = result * PRIME + ($current == null ? 43 : $current.hashCode());
        final Object $size = this.getSize();
        result = result * PRIME + ($size == null ? 43 : $size.hashCode());
        final Object $total = this.getTotal();
        result = result * PRIME + ($total == null ? 43 : $total.hashCode());
        final Object $records = this.getRecords();
        result = result * PRIME + ($records == null ? 43 : $records.hashCode());
        return result;
    }

    public String toString() {
        return "PageResponse(current=" + this.getCurrent() + ", size=" + this.getSize() + ", total=" + this.getTotal() + ", records=" + this.getRecords() + ")";
    }
}
