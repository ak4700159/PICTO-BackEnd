package picto.com.photomanager.global.utils;

import picto.com.photomanager.domain.photo.entity.Photo;

import java.util.Comparator;

public class PhotoViewComparator implements Comparator<Photo> {
    @Override
    public int compare(Photo p1, Photo p2) {
        if(p1.getViews() > p2.getViews()){
            return 1;
        }
        else if(p1.getViews() < p2.getViews()){
            return -1;
        }
        return 0;
    }
}