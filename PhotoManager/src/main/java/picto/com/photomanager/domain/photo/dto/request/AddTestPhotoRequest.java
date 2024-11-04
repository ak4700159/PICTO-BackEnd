package picto.com.photomanager.domain.photo.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picto.com.photomanager.domain.photo.entity.Photo;
import picto.com.photomanager.domain.photo.entity.PhotoId;
import picto.com.photomanager.global.user.entity.User;

import java.util.Random;

import static picto.com.photomanager.global.user.entity.QUser.user;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTestPhotoRequest {
    private PhotoId photoId;
    private String photoPath;
    private double lat;
    private double lng;
    private String location;
    private Long registerTime;
    private Long updateTime;
    private boolean frame_active;
    private boolean shared_active;
    private int likes;
    private int views;
    private String title;
    private String tag;

    public Photo toRandomPhoto(int userIdNum, int photoIdNum, User user){
        Random random = new Random();
        location = "대구시 달성군 옥포읍";
        photoPath = "s3://picto-test-bucket/picto-photos/20210115_104549.jpg";

        likes = random.nextInt(10000);
        views = random.nextInt(10000);

        lat = random.nextDouble(35.88682728 - 35.77475029) + 35.77475029;
        lng = random.nextDouble(128.6355584 - 128.4313995) +  128.4313995;

        frame_active = false;
        shared_active = true;

        title = userIdNum + "'s photo title";
        updateTime = System.currentTimeMillis();
        registerTime = System.currentTimeMillis();

        photoId = new PhotoId(photoIdNum, userIdNum);
        tag = "TEST";

        return Photo
                .builder()
                .tag(tag)
                .user(user)
                .photoId(photoId)
                .title(title)
                .lat(lat)
                .lng(lng)
                .registerTime(registerTime)
                .uploadTime(updateTime)
                .likes(likes)
                .views(views)
                .location(location)
                .photoPath(photoPath)
                .build();
    }
}
