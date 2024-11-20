package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Builder;
import lombok.Getter;
import picto.com.usermanager.domain.user.entity.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetUserInfoResponse {
    private GetUser user;
    private GetFilter filter;
    private GetSetting userSetting;
    private List<GetTag> tags;
    private List<GetTitle> titles;
    private List<GetPhoto> photos;
    private List<Long> marks;
    private List<Long> blocks;

    @Builder
    public GetUserInfoResponse(User user, UserSetting setting, Filter filter, List<Photo> photos, List<Title> titles,
                               List<TagSelect> tags, List<Mark> marks, List<Block> blocks) {
        this.user = new GetUser(user);
        this.filter = new GetFilter(filter);
        this.userSetting = new GetSetting(setting);

        this.tags = new ArrayList<GetTag>();
        for(TagSelect tag : tags){
            this.tags.add(new GetTag(tag));
        }

        this.photos = new ArrayList<GetPhoto>();
        for(Photo photo : photos){
            this.photos.add(new GetPhoto(photo));
        }

        this.titles = new ArrayList<>();
        for(Title title : titles){
            this.titles.add(new GetTitle(title));
        }

        this.marks = new ArrayList<>();
        for(Mark mark : marks){
            this.marks.add(mark.getMarked().getId());
        }

        this.blocks = new ArrayList<>();
        for(Block block : blocks){
            this.blocks.add(block.getBlocked().getId());
        }
    }

}
