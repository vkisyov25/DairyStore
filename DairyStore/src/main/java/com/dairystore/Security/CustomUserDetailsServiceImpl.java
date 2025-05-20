package com.dairystore.Security;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.CreateUserDto;
import com.dairystore.Models.dtos.LoginUserDto;
import com.dairystore.Repository.UserRepository;
import com.dairystore.Services.EmailService;
import com.dairystore.Services.StripeService;
import com.dairystore.Utils.JwtUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletResponse response;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;
    private final StripeService stripeService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Зареждане на данни за потребителя
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // Генериране на JWT токен
        String token = jwtUtil.generateToken(customUserDetails);

        String role = customUserDetails.getAuthorities().iterator().next().getAuthority();
        //Collection<? extends GrantedAuthority> cc = authentication.getAuthorities();


        deleteCookies();

        // Съхраняване на JWT токен в HTTP-only cookie
        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 час
        response.addCookie(cookie);
        return new CustomUserDetails(user);


    }

    @Override
    public void deleteCookies() {
        request.getSession().invalidate(); // Това ще изтрие текущата сесия
        // Изтриваме стария токен, ако има такъв
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    cookie.setValue(null); // Изтриваме стойността
                    cookie.setMaxAge(0);   // Изтриваме cookie-то
                    cookie.setPath("/");   // Уверяваме се, че е изтрито на глобално ниво
                    response.addCookie(cookie); // Изпращаме обратно изтритото cookie
                }
            }
        }
    }

    @Override
    public void create(CreateUserDto createUserDto) throws Exception {
        User user = mapToUser(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        if(createUserDto.getAuthorities().equals("seller")){
            String accountId = stripeService.createConnectedAccount(createUserDto.getEmail());
            System.out.println(accountId);
            user.setAccountId(accountId);
            String onboardingLink = stripeService.generateOnboardingLink(accountId);
            emailService.sendRegistrationEmailToSeller(createUserDto.getEmail(), createUserDto.getName(), onboardingLink);

        }else {
            emailService.sendRegistrationEmailToBuyer(createUserDto.getEmail(), createUserDto.getName());
        }
        userRepository.save(user);
    }

    public void isCompanyInfoValid(String eik, String name, BindingResult bindingResult) {
        if (eik.isEmpty() || name.isEmpty()) {
            return;
        }
        WebDriverManager.chromedriver().setup(); // изтегля съвместим chromedriver() за текущата версия на chrome
        ChromeOptions options = new ChromeOptions();
        /*options.addArguments("--headless=new");*/
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options); //стартираме Chrome браузъра с форните опции чрез Selenium WebDriver


        boolean isValid;

        try {
            driver.get("https://portal.registryagency.bg/CR/en/Reports/VerificationPersonOrg");

            // Избиране на "Legal entity"
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1)); //Създаваш „изчакване“, което ще чака до 10 секунди даден елемент да се появи на страницата
            //ExpectedConditions.presenceOfElementLocated - "Чакай да се появи елемент на страницата, който отговаря на даден селектор".
            WebElement label = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("label[for='CompanyFormId']")
            ));
            label.click();

            WebElement inputEik = null;
            WebElement inputName = null;

            try {
                //ExpectedConditions.visibilityOfElementLocated - "Намери елемент по даден селектор и изчакай той да стане видим на страницата."
                //presenceOfElementLocated ➜ чака елементът да съществува в DOM-а (но може да е скрит)
                //visibilityOfElementLocated ➜ чака елементът да е и видим, и достъпен за взаимодействие
                inputEik = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchCriteria_ident")));
                inputName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchCriteria_name")));
            } catch (Exception ex) {
                System.out.println("Не успя да намери полето за ЕИК или за името на фирмата");
                ex.printStackTrace();
            }

            // Попълване и търсене
            assert inputEik != null;
            inputEik.clear();
            inputEik.sendKeys(eik);
            assert inputName != null;
            inputName.clear();
            inputName.sendKeys(name);

            //ExpectedConditions.elementToBeClickable - Изчакай елементът: да съществува в DOM-а, да бъде видим и включен (enabled) — т.е. готов да бъде натиснат с мишката“
            WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Search')]")
            ));
            searchBtn.click();

            // Изчакване на резултата да се появи с нужния текст
            isValid = wait.until(driver1 -> {
                WebElement div = driver1.findElement(By.cssSelector("div.result-count"));
                String text = div.getText();
                return text != null && text.contains("Total: 1");
            });

        } catch (Exception e) {
            isValid = false;
        } finally {
            driver.quit();
        }

        if (!isValid) {
            bindingResult.rejectValue("companyEIK", "companyEik-error", "Проверете еик на фирмата");
            bindingResult.rejectValue("companyName", "companyName-error", "Проверете името на фирмата");
        }
    }

    private User mapToUser(CreateUserDto dto) {
        return User.builder().username(dto.getUsername()).password(dto.getPassword()).email(dto.getEmail()).name(dto.getName()).phone(dto.getPhone()).address(dto.getAddress()).companyName(dto.getCompanyName()).companyEIK(dto.getCompanyEIK()).authorities(dto.getAuthorities()).build();
    }


    @Override
    public void validateUser(CreateUserDto user, BindingResult bindingResult) {

        //проверката с ролите е заради това, че при buyer companyName и companyEIK могат да са празни и като има потребител с празни
        // companyName и companyEIK в базата данни и като искам да запиша нов ми показва, че вече съществуват
        if (user.getAuthorities().equals("seller")) {
            //По принцип seller няма как да има записани потребители с празно companyName, но метода existsByCompanyName() проверява за
            //всички потребители а buyer могат да имат и затова слагам !user.getCompanyName().isEmpty()
            if (existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Фирмата вече съществува");
            }

            if (existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "Компанията с това EIK вече съществува");
            }

        }

        if (user.getAuthorities().equals("buyer")) {
            if (existsByCompanyName(user.getCompanyName()) && !user.getCompanyName().isEmpty()) {
                bindingResult.rejectValue("companyName", "error.companyName", "Фирмата вече съществува");
            }

            if (existsByCompanyEIK(user.getCompanyEIK()) && !user.getCompanyEIK().isEmpty()) {
                bindingResult.rejectValue("companyEIK", "error.companyEIK", "Компанията с това EIK вече съществува");
            }

        }

        if (existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Имейл адресът вече съществува");
        }

        if (existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Потребителското име вече съществува");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCompanyName(String companyName) {
        return userRepository.existsByCompanyName(companyName);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByCompanyEIK(String companyEIK) {
        return userRepository.existsByCompanyEIK(companyEIK);
    }

    @Override
    public String roleExtraction(@NotNull LoginUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        UserDetails userDetails = loadUserByUsername(username);
        return userDetails.getAuthorities().iterator().next().getAuthority();
    }

    @Override
    public boolean passwordValidator(@NotNull LoginUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();
        UserDetails userDetails = loadUserByUsername(username);
        String storedPassword = userDetails.getPassword();
        return passwordEncoder.matches(password, storedPassword);

    }
}
