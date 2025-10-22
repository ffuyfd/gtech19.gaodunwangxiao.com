package com.example.gtech19.common;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class PageResponse<T> {
    @ApiModelProperty("分页数据集")
    private List<T> list;
    @ApiModelProperty("当前页码")
    private Integer pageNum;
    @ApiModelProperty("每页数量")
    private Integer pageSize;
    @ApiModelProperty("当前页的数量")
    private Integer size;
    @ApiModelProperty("记录总数")
    private Long totalSize;
    @ApiModelProperty("页码总数")
    private Integer totalPages;

    public static <T> PageResponse<T> build(PageInfo<T> pageInfo) {
        if (Objects.isNull(pageInfo)) {
            return null;
        } else {
            PageResponse<T> response = new PageResponse();
            response.pageNum = pageInfo.getPageNum();
            response.pageSize = pageInfo.getPageSize();
            response.size = pageInfo.getSize();
            response.totalPages = pageInfo.getPages();
            response.totalSize = pageInfo.getTotal();
            response.list = pageInfo.getList();
            return response;
        }
    }

    public static <T> PageResponse<T> build(PageInfo<T> pageInfo, List<T> list) {
        if (Objects.isNull(pageInfo)) {
            return null;
        } else {
            PageResponse<T> response = new PageResponse();
            response.pageNum = pageInfo.getPageNum();
            response.pageSize = pageInfo.getPageSize();
            response.size = pageInfo.getSize();
            response.totalPages = pageInfo.getPages();
            response.totalSize = CollectionUtils.isEmpty(list) ? 0L : pageInfo.getTotal();
            response.list = list;
            return response;
        }
    }

    public static <T, R> PageResponse<R> build(PageInfo<T> pageInfo, Function<T, R> transformFunction) {
        if (Objects.isNull(pageInfo)) {
            return null;
        } else {
            PageResponse<R> response = new PageResponse();
            response.pageNum = pageInfo.getPageNum();
            response.pageSize = pageInfo.getPageSize();
            response.size = pageInfo.getSize();
            response.totalPages = pageInfo.getPages();
            response.totalSize = pageInfo.getTotal();
            response.list = (List)pageInfo.getList().stream().map(transformFunction).collect(Collectors.toList());
            return response;
        }
    }

    public static <T> PageResponse<T> empty(int pageNum, int pageSize) {
        PageResponse<T> response = new PageResponse();
        response.pageNum = pageNum;
        response.pageSize = pageSize;
        response.list = new ArrayList<>();
        return response;
    }

    public static <T> PageResponse<T> composePage(int pageNum, int pageSize, int currentSize, long totalSize, List<T> list) {
        PageResponse<T> response = new PageResponse();
        response.pageNum = pageNum;
        response.pageSize = pageSize;
        response.size = currentSize;
        response.list = list;
        response.totalSize = totalSize;
        return response;
    }

    protected PageResponse(final PageResponseBuilder<T, ?, ?> b) {
        this.list = b.list;
        this.pageNum = b.pageNum;
        this.pageSize = b.pageSize;
        this.size = b.size;
        this.totalSize = b.totalSize;
        this.totalPages = b.totalPages;
    }

    public static <T> PageResponseBuilder<T, ?, ?> builder() {
        return new PageResponseBuilderImpl();
    }

    public List<T> getList() {
        return this.list;
    }

    public Integer getPageNum() {
        return this.pageNum;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Integer getSize() {
        return this.size;
    }

    public Long getTotalSize() {
        return this.totalSize;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public void setList(final List<T> list) {
        this.list = list;
    }

    public void setPageNum(final Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setSize(final Integer size) {
        this.size = size;
    }

    public void setTotalSize(final Long totalSize) {
        this.totalSize = totalSize;
    }

    public void setTotalPages(final Integer totalPages) {
        this.totalPages = totalPages;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResponse)) {
            return false;
        } else {
            PageResponse<?> other = (PageResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$pageNum = this.getPageNum();
                Object other$pageNum = other.getPageNum();
                if (this$pageNum == null) {
                    if (other$pageNum != null) {
                        return false;
                    }
                } else if (!this$pageNum.equals(other$pageNum)) {
                    return false;
                }

                Object this$pageSize = this.getPageSize();
                Object other$pageSize = other.getPageSize();
                if (this$pageSize == null) {
                    if (other$pageSize != null) {
                        return false;
                    }
                } else if (!this$pageSize.equals(other$pageSize)) {
                    return false;
                }

                Object this$size = this.getSize();
                Object other$size = other.getSize();
                if (this$size == null) {
                    if (other$size != null) {
                        return false;
                    }
                } else if (!this$size.equals(other$size)) {
                    return false;
                }

                label62: {
                    Object this$totalSize = this.getTotalSize();
                    Object other$totalSize = other.getTotalSize();
                    if (this$totalSize == null) {
                        if (other$totalSize == null) {
                            break label62;
                        }
                    } else if (this$totalSize.equals(other$totalSize)) {
                        break label62;
                    }

                    return false;
                }

                label55: {
                    Object this$totalPages = this.getTotalPages();
                    Object other$totalPages = other.getTotalPages();
                    if (this$totalPages == null) {
                        if (other$totalPages == null) {
                            break label55;
                        }
                    } else if (this$totalPages.equals(other$totalPages)) {
                        break label55;
                    }

                    return false;
                }

                Object this$list = this.getList();
                Object other$list = other.getList();
                if (this$list == null) {
                    if (other$list != null) {
                        return false;
                    }
                } else if (!this$list.equals(other$list)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageResponse;
    }

    public int hashCode() {
        int result = 1;
        Object $pageNum = this.getPageNum();
        result = result * 59 + ($pageNum == null ? 43 : $pageNum.hashCode());
        Object $pageSize = this.getPageSize();
        result = result * 59 + ($pageSize == null ? 43 : $pageSize.hashCode());
        Object $size = this.getSize();
        result = result * 59 + ($size == null ? 43 : $size.hashCode());
        Object $totalSize = this.getTotalSize();
        result = result * 59 + ($totalSize == null ? 43 : $totalSize.hashCode());
        Object $totalPages = this.getTotalPages();
        result = result * 59 + ($totalPages == null ? 43 : $totalPages.hashCode());
        Object $list = this.getList();
        result = result * 59 + ($list == null ? 43 : $list.hashCode());
        return result;
    }

    public String toString() {
        return "PageResponse(list=" + this.getList() + ", pageNum=" + this.getPageNum() + ", pageSize=" + this.getPageSize() + ", size=" + this.getSize() + ", totalSize=" + this.getTotalSize() + ", totalPages=" + this.getTotalPages() + ")";
    }

    public PageResponse(final List<T> list, final Integer pageNum, final Integer pageSize, final Integer size, final Long totalSize, final Integer totalPages) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.size = size;
        this.totalSize = totalSize;
        this.totalPages = totalPages;
    }

    public PageResponse() {
    }

    public abstract static class PageResponseBuilder<T, C extends PageResponse<T>, B extends PageResponseBuilder<T, C, B>> {
        private List<T> list;
        private Integer pageNum;
        private Integer pageSize;
        private Integer size;
        private Long totalSize;
        private Integer totalPages;

        public PageResponseBuilder() {
        }

        protected abstract B self();

        public abstract C build();

        public B list(final List<T> list) {
            this.list = list;
            return this.self();
        }

        public B pageNum(final Integer pageNum) {
            this.pageNum = pageNum;
            return this.self();
        }

        public B pageSize(final Integer pageSize) {
            this.pageSize = pageSize;
            return this.self();
        }

        public B size(final Integer size) {
            this.size = size;
            return this.self();
        }

        public B totalSize(final Long totalSize) {
            this.totalSize = totalSize;
            return this.self();
        }

        public B totalPages(final Integer totalPages) {
            this.totalPages = totalPages;
            return this.self();
        }

        public String toString() {
            return "PageResponse.PageResponseBuilder(list=" + this.list + ", pageNum=" + this.pageNum + ", pageSize=" + this.pageSize + ", size=" + this.size + ", totalSize=" + this.totalSize + ", totalPages=" + this.totalPages + ")";
        }
    }

    private static final class PageResponseBuilderImpl<T> extends PageResponseBuilder<T, PageResponse<T>, PageResponseBuilderImpl<T>> {
        private PageResponseBuilderImpl() {
        }

        protected PageResponseBuilderImpl<T> self() {
            return this;
        }

        public PageResponse<T> build() {
            return new PageResponse(this);
        }
    }
}
