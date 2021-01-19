package tacos;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class) // Spring MVC 형태에서의 테스트 실행
public class HomeControllerTest {
    /**
     * 실제 서버 구동 대신 Mocking을 사용하여 테스트를 진행한다
     */
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))      // GET /를 수행한다

                .andExpect(status().isOk())     // HTTP 200이 되어야 한다

                .andExpect(view().name("home"))  // home 뷰가 있어야 한다

                .andExpect(content().string(       // 콘텐츠에 Welcome to...가 포함되어야 한다
                        containsString("Welcome to...")));
    }
}