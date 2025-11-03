package lotto;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.*;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        List<Integer> copy = new ArrayList<>(numbers);
        Collections.sort(copy);
        this.numbers = Collections.unmodifiableList(copy);
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        if (new HashSet<>(numbers).size() != numbers.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 중복될 수 없습니다.");
        }
        for (int n : numbers) {
            if (n < 1 || n > 45) {
                throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
            }
        }
    }

    // TODO: 추가 기능 구현

    /** 자동 발행 */
    public static Lotto auto() {
        List<Integer> nums = new ArrayList<>(Randoms.pickUniqueNumbersInRange(1, 45, 6));
        Collections.sort(nums);
        return new Lotto(nums);
    }

    /** 오름차순 정렬된 읽기 전용 번호 목록 */
    public List<Integer> getNumbers() {
        return numbers;
    }

    /** 당첨 번호와의 일치 개수 */
    public int matchCount(Set<Integer> winning) {
        int cnt = 0;
        for (int n : numbers) {
            if (winning.contains(n))
                cnt++;
        }
        return cnt;
    }

    /** 특정 번호 포함 여부 (보너스 판정용) */
    public boolean contains(int n) {
        return numbers.contains(n);
    }

    @Override
    public String toString() {
        return numbers.toString();
    }
}