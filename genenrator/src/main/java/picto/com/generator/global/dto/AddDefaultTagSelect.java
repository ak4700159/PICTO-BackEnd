package picto.com.generator.global.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.generator.domain.user.domain.User;
import picto.com.generator.global.models.TagSelect;
import picto.com.generator.global.models.TagSelectId;

@Getter
@NoArgsConstructor
public class AddDefaultTagSelect {
    public TagSelect toEntity(User newUser, String tagName) {
        return TagSelect.builder()
                .user(newUser)
                .id(new TagSelectId(tagName, newUser.getUser_id()))
                .build();
    }
}
