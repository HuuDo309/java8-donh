package com.digidinos.shopping.pagination;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

public class PaginationResult<E> {

    private int totalRecords;
    private int currentPage;
    private List<E> list;
    private int maxResult;
    private int totalPages;

    private int maxNavigationPage;

    private List<Integer> navigationPages;

    // Constructor mới sử dụng TypedQuery
    public PaginationResult(TypedQuery<E> query, int page, int maxResult, int maxNavigationPage) {
        final int pageIndex = page - 1 < 0 ? 0 : page - 1;

        int fromRecordIndex = pageIndex * maxResult;

        TypedQuery<Long> countQuery = (TypedQuery<Long>) query;
        countQuery.setFirstResult(0);
        countQuery.setMaxResults(Integer.MAX_VALUE);
        long totalRecordsLong = countQuery.getResultList().size();
        this.totalRecords = (int) totalRecordsLong;

        // Lấy dữ liệu cho trang hiện tại
        query.setFirstResult(fromRecordIndex);
        query.setMaxResults(maxResult);
        this.list = query.getResultList();

        this.currentPage = pageIndex + 1;
        this.maxResult = maxResult;

        if (this.totalRecords % this.maxResult == 0) {
            this.totalPages = this.totalRecords / this.maxResult;
        } else {
            this.totalPages = (this.totalRecords / this.maxResult) + 1;
        }

        this.maxNavigationPage = maxNavigationPage;

        if (this.maxNavigationPage > this.totalPages) {
            this.maxNavigationPage = this.totalPages;
        }

        this.calcNavigationPages();
    }

    private void calcNavigationPages() {
        this.navigationPages = new ArrayList<>();

        int current = Math.min(this.currentPage, this.totalPages);
        int begin = Math.max(current - this.maxNavigationPage / 2, 1);
        int end = Math.min(current + this.maxNavigationPage / 2, this.totalPages);

        // Luôn thêm trang đầu tiên
        navigationPages.add(1);
        if (begin > 2) {
            navigationPages.add(-1); // Dùng cho '...'
        }

        for (int i = begin; i <= end; i++) {
            if (i > 1 && i < this.totalPages) {
                navigationPages.add(i);
            }
        }

        if (end < this.totalPages - 1) {
            navigationPages.add(-1); // Dùng cho '...'
        }
        // Luôn thêm trang cuối cùng
        if (this.totalPages > 1) {
            navigationPages.add(this.totalPages);
        }
    }

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<E> getList() {
		return list;
	}

	public int getMaxResult() {
		return maxResult;
	}

	public List<Integer> getNavigationPages() {
		return navigationPages;
	}

}