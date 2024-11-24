package picto.com.usermanager.domain.user.dto.response.get.userInfo;

import lombok.Builder;
import lombok.Getter;
import picto.com.usermanager.domain.user.dao.ShareRepository;
import picto.com.usermanager.domain.user.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class GetUserInfoResponse {
    //private final ShareRepository shareRepository;
    private final GetUser user;
    private final GetFilter filter;
    private final GetSetting userSetting;
    private final List<GetTag> tags;
    private final List<GetTitle> titles;
    private final List<GetPhoto> photos;
    private final List<Long> marks;
    private final List<Long> blocks;
    private final List<GetFolder> folders;

    @Builder
    public GetUserInfoResponse(User user, UserSetting setting, Filter filter, List<Photo> photos, List<Title> titles,
                               List<TagSelect> tags, List<Mark> marks, List<Block> blocks, List<Folder> folders, List<Share> shares) {
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

        this.folders = new ArrayList<>();
        for(Folder folder : folders){
            List<Long> members = new ArrayList<>();
            for(Share share : shares){
                if(Objects.equals(folder.getFolderId(), share.getId().getFolderId())) {
                    members.add(share.getId().getUserId());
                }
            }
            this.folders.add(new GetFolder(folder, members));
        }
    }

}
