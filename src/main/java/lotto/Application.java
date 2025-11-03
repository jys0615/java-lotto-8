package lotto;

import camp.nextstep.edu.missionutils.Console;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    private enum Rank {
        FIRST(6, false, 2_000_000_000L),
        SECOND(5, true, 30_000_000L),
        THIRD(5, false, 1_500_000L),
        FOURTH(4, false, 50_000L),
        FIFTH(3, false, 5_000L);

        private final int matchCount;
        private final boolean requiresBonus;
        private final long prize;

        Rank(int matchCount, boolean requiresBonus, long prize) {
            this.matchCount = matchCount;
            this.requiresBonus = requiresBonus;
            this.prize = prize;
        }

        public long prize() {
            return prize;
        }

        public static Optional<Rank> of(int matches, boolean bonusMatched) {
            if (matches == 6)
                return Optional.of(FIRST);
            if (matches == 5 && bonusMatched)
                return Optional.of(SECOND);
            if (matches == 5)
                return Optional.of(THIRD);
            if (matches == 4)
                return Optional.of(FOURTH);
            if (matches == 3)
                return Optional.of(FIFTH);
            return Optional.empty();
        }
    }

    public static void main(String[] args) {
        // TODO: 프로그램 구현
        try {
            int amount = readPurchaseAmount();
            int count = amount / 1000;

            List<Lotto> tickets = issueTickets(count);
            System.out.println();
            System.out.println(count + "개를 구매했습니다.");
            printTickets(tickets);

            System.out.println();
            System.out.println("당첨 번호를 입력해 주세요.");
            Set<Integer> winning = readWinningNumbers();

            System.out.println();
            System.out.println("보너스 번호를 입력해 주세요.");
            int bonus = readBonusNumber(winning);

            System.out.println();
            printStatistics(tickets, winning, bonus, amount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            main(null); // 에러 메시지 출력 후 해당 단계부터 재입력 받기 위해 재시작
        }
    }

    private static int readPurchaseAmount() {
        System.out.println("구입금액을 입력해 주세요.");
        String line = Console.readLine();
        int amount = parsePositiveInt(line, "[ERROR] 구입 금액은 숫자여야 합니다.");
        validateAmount(amount);
        return amount;
    }

    private static void validateAmount(int amount) {
        if (amount < 1000) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 이상이어야 합니다.");
        }
        if (amount % 1000 != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위여야 합니다.");
        }
    }

    private static int parsePositiveInt(String s, String errorMessage) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static List<Lotto> issueTickets(int count) {
        List<Lotto> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(Lotto.auto());
        }
        return tickets;
    }

    private static void printTickets(List<Lotto> tickets) {
        for (Lotto t : tickets) {
            System.out.println(t.getNumbers());
        }
    }

    private static Set<Integer> readWinningNumbers() {
        String line = Console.readLine();
        List<Integer> nums = parseCommaSeparatedInts(line);
        if (nums.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 쉼표로 구분된 6개의 숫자여야 합니다.");
        }
        validateRange(nums);
        validateNoDuplicate(nums);
        return new HashSet<>(nums);
    }

    private static int readBonusNumber(Set<Integer> winning) {
        String line = Console.readLine();
        int bonus = parsePositiveInt(line, "[ERROR] 보너스 번호는 숫자여야 합니다.");
        if (bonus < 1 || bonus > 45) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        if (winning.contains(bonus)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
        return bonus;
    }

    private static List<Integer> parseCommaSeparatedInts(String s) {
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .map(x -> parsePositiveInt(x, "[ERROR] 로또 번호는 숫자여야 합니다."))
                .collect(Collectors.toList());
    }

    private static void validateRange(List<Integer> nums) {
        for (int n : nums) {
            if (n < 1 || n > 45) {
                throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
            }
        }
    }

    private static void validateNoDuplicate(List<Integer> nums) {
        if (new HashSet<>(nums).size() != nums.size()) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 중복될 수 없습니다.");
        }
    }

    private static void printStatistics(List<Lotto> tickets, Set<Integer> winning, int bonus, int amount) {
        Map<Rank, Integer> counter = new EnumMap<>(Rank.class);
        for (Lotto t : tickets) {
            int matches = t.matchCount(winning);
            boolean bonusMatched = t.contains(bonus);
            Rank.of(matches, bonusMatched).ifPresent(rank -> counter.put(rank, counter.getOrDefault(rank, 0) + 1));
        }

        System.out.println("당첨 통계");
        System.out.println("---");
        int fifth = counter.getOrDefault(Rank.FIFTH, 0);
        int fourth = counter.getOrDefault(Rank.FOURTH, 0);
        int third = counter.getOrDefault(Rank.THIRD, 0);
        int second = counter.getOrDefault(Rank.SECOND, 0);
        int first = counter.getOrDefault(Rank.FIRST, 0);

        System.out.println("3개 일치 (5,000원) - " + fifth + "개");
        System.out.println("4개 일치 (50,000원) - " + fourth + "개");
        System.out.println("5개 일치 (1,500,000원) - " + third + "개");
        System.out.println("5개 일치, 보너스 볼 일치 (30,000,000원) - " + second + "개");
        System.out.println("6개 일치 (2,000,000,000원) - " + first + "개");

        long revenue = 0;
        revenue += (long) fifth * Rank.FIFTH.prize();
        revenue += (long) fourth * Rank.FOURTH.prize();
        revenue += (long) third * Rank.THIRD.prize();
        revenue += (long) second * Rank.SECOND.prize();
        revenue += (long) first * Rank.FIRST.prize();

        double rate = (double) revenue / amount * 100.0;
        System.out.printf("총 수익률은 %.1f%%입니다.%n", roundOneDecimal(rate));
    }

    private static double roundOneDecimal(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}