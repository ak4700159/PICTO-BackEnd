package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Getter;
import picto.com.usermanager.domain.user.entity.Filter;

@Getter
public class GetFilter {
    // "좋아요순" "조회수순"
    String sort;
    // 하루/일주일/한달/일년/사용자지정/ALL
    String period;
    // 사용자 지정 설정된 시작 시간
    Long startDatetime;
    // 사용자 지정 설정된 끝 시간
    Long endDatetime;

    public GetFilter(Filter filter){
        this.sort = filter.getSort();
        this.period = filter.getPeriod();
    }
}
