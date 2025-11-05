package com.example.UserService.repository;

import com.example.UserService.model.entity.CardInfo;
import com.example.UserService.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Test
    void testSaveAndFindUserByEmail() {
        User user = User.builder()
                .name("Nikita")
                .surname("Kyst")
                .birthDate(LocalDateTime.now().minusYears(20))
                .email("nikita@example.com")
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByEmail("nikita@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("nikita@example.com");
    }

    @Test
    void testFindAllUsersByIds() {
        User u1 = User.builder().name("A").surname("B")
                .birthDate(LocalDateTime.now().minusYears(20))
                .email("a@example.com").build();
        User u2 = User.builder().name("C").surname("D")
                .birthDate(LocalDateTime.now().minusYears(19))
                .email("c@example.com").build();
        userRepository.saveAll(List.of(u1, u2));

        List<User> found = userRepository.findAllByIds(List.of(u1.getId(), u2.getId()));
        assertThat(found).hasSize(2);
    }

    @Test
    void testSaveCardAndFindByNumber() {
        User user = User.builder()
                .name("Nikita")
                .surname("Kyst")
                .birthDate(LocalDateTime.of(1990, 1, 1, 0, 0))
                .email("nikita@example.com")
                .build();
        userRepository.save(user);

        CardInfo card = CardInfo.builder()
                .number("1234567890123456")
                .expirationDate(LocalDateTime.now().plusYears(2))
                .holder("Nikita Kyst")
                .user(user)
                .build();

        cardInfoRepository.save(card);

        Optional<CardInfo> found = cardInfoRepository.findByNumber("1234567890123456");
        assertThat(found).isPresent();
        assertThat(found.get().getHolder()).isEqualTo("Nikita Kyst");
    }
}
