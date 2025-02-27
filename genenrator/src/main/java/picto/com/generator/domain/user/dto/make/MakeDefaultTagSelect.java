package picto.com.generator.domain.user.dto.make;


import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.domain.user.entity.TagSelect;
import picto.com.generator.domain.user.entity.TagSelectId;

@Getter
@NoArgsConstructor
public class MakeDefaultTagSelect {
    public TagSelect toEntity(User newUser, String tagName) {
        return TagSelect.builder()
                .user(newUser)
                .tagSelectedId(new TagSelectId(tagName, newUser.getUserId()))
                .build();
    }
}
