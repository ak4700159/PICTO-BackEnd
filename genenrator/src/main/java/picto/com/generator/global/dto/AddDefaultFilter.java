package picto.com.generator.global.dto;

import lombok.*;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.Filter;

import java.util.Date;

@Getter
@NoArgsConstructor
public class AddDefaultFilter {

    // 처음 생성시 "좋아요순" "한 달"
    public Filter toEntity(User newUser){
        return Filter.builder().
                user(newUser).
                sort("좋아요순").
                period("한 달").
                startDate(Long.valueOf(new Date().getTime()).intValue()).
                build();
    }
}
