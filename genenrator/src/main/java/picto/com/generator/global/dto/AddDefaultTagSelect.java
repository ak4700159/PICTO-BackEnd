package picto.com.generator.global.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.entity.User;
import picto.com.generator.global.entity.TagSelect;
import picto.com.generator.global.entity.TagSelectId;

@Getter
@NoArgsConstructor
public class AddDefaultTagSelect {
    public TagSelect toEntity(User newUser, String tagName) {
        return TagSelect.builder()
                .user(newUser)
                .tagSelectedId(new TagSelectId(tagName, newUser.getUserId()))
                .build();
    }
}
