package picto.com.generator.domain.user.dto.make;

import lombok.*;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.domain.user.entity.Filter;

@Getter
@NoArgsConstructor
public class MakeDefaultFilter {

    // 처음 생성시 "좋아요순" "한달"
    public Filter toEntity(User newUser){
        return Filter.builder().
                user(newUser).
                sort("좋아요순").
                period("한달").
                build();
    }
}
