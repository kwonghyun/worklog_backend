package com.example.worklog.dto.work;

import com.example.worklog.dto.enums.SortParam;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomDateValid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class WorkGetRequestParamDto {
    private int pageNum;

    private int pageSize;

    @CustomDateValid
    private String startDate;

    @CustomDateValid
    private String endDate;

    private SortParam.Direction direction;

    private SortParam.SortBy sortBy;

    private String keyword;

    private Category category;
    private WorkState state;

    public WorkGetRequestParamDto(
            @RequestParam(name = "page") Integer pageNum,
            @RequestParam(name = "size") Integer pageSize,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "key") String keyword,
            SortParam.SortBy sortBy,
            SortParam.Direction direction,
            @RequestParam(name = "category") Category category,
            @RequestParam(name = "state") WorkState state
    ) {
        this.pageNum = pageNum == null ? 1 : pageNum;
        this.pageSize = pageSize == null ? 10 : pageSize;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortBy = sortBy == null ? SortParam.SortBy.ORDER : sortBy;
        this.direction = direction == null ? SortParam.Direction.DESC : direction;
        this.keyword = keyword;
        this.category = category;
        this.state = state;
    }
}