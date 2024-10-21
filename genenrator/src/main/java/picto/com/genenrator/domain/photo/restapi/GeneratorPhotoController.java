package picto.com.genenrator.domain.photo.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GeneratorPhotoController {

    // RestController 애너테이션을 클래스에 붙이면 HTTP 응답으로 객체 데이터를 JSON 형식으로 반환한다.
    // @RequestBody 붙인 객체인 AddArticleRequest 객체에 자동으로 body의 json 데이터와 매핑하여 객체를 생성한다.
//    @PostMapping("/generator/photos")
//    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
//
//    }


//    @GetMapping("/generator/photos")
//    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
//
//    }

}
