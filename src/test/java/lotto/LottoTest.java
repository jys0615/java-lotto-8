package lotto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class LottoTest {
    @Test
    void 로또_번호의_개수가_6개가_넘어가면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 6, 7)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("로또 번호에 중복된 숫자가 있으면 예외가 발생한다.")
    @Test
    void 로또_번호에_중복된_숫자가_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 5)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // TODO: 추가 기능 구현에 따른 테스트 코드 작성

    @DisplayName("로또 번호는 1~45 범위 안의 숫자여야 한다.")
    @Test
    void 로또_번호가_범위를_벗어나면_예외가_발생한다() {
        assertThatThrownBy(() -> new Lotto(List.of(0, 2, 3, 4, 5, 6)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자동 로또 발행 시 6개의 고유한 번호가 생성된다.")
    @Test
    void 자동_로또_발행() {
        Lotto lotto = Lotto.auto();
        assertThat(lotto.getNumbers()).hasSize(6);
        assertThat(lotto.getNumbers()).doesNotHaveDuplicates();
        assertThat(lotto.getNumbers()).allMatch(n -> n >= 1 && n <= 45);
    }

    @DisplayName("당첨 번호와 일치하는 개수를 정확히 계산한다.")
    @Test
    void 일치_개수_계산() {
        Lotto lotto = new Lotto(List.of(1, 2, 3, 10, 20, 30));
        Set<Integer> winning = Set.of(1, 2, 3, 4, 5, 6);
        assertThat(lotto.matchCount(winning)).isEqualTo(3);
    }

    @DisplayName("보너스 번호 포함 여부를 정확히 확인한다.")
    @Test
    void 보너스_번호_포함_확인() {
        Lotto lotto = new Lotto(List.of(7, 8, 9, 10, 11, 12));
        assertThat(lotto.contains(10)).isTrue();
        assertThat(lotto.contains(45)).isFalse();
    }
}