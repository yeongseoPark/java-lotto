package lotto.domain;

import lotto.Lotto;
import lotto.prizestandards.PrizeStandards;
import lotto.ui.input.WinnerNumberGetter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrizeListGenerator {
    public Map<Enum, Integer> winnerResult = new HashMap<>();
    private IssuingMachine issuingMachine = new IssuingMachine();
    private int bonus;

    private WinnerNumberGetter winnerNumberGetter = new WinnerNumberGetter();

    public Map<Integer, Integer> getWinningNumbers() {
        Map<Integer,Integer> winningNumbers = winnerNumberGetter.getLotteryWinningNumbersInput();
        bonus = winnerNumberGetter.getBonusInput();

        return winningNumbers;
    }

    public void iterateLotteriesForStatistic(Map<Integer, Integer> winningNumbers, List<Lotto> lotteries) {
        int score = 0;

        for (Lotto lottery : lotteries) { // 당첨 계산하는 제일 핵심로직  시간복잡도 개선방법 생각해보기
            List<Integer> numbers = lottery.getNumbers();

            for (int eachNum : numbers) {
                if (winningNumbers.containsKey(eachNum)) {
                    score += 2;
                }
            }

            if (score == 10 && numbers.contains(bonus)) {
                score += 1;
            }

            Enum<PrizeStandards> prizeStandardsEnum = getCorrespondingPrizeStandard(score);
            winnerResult.computeIfPresent(prizeStandardsEnum, (k,v) -> v+1);
            winnerResult.computeIfAbsent(prizeStandardsEnum, v -> 1);

            score = 0;
        }

        for (PrizeStandards i : PrizeStandards.values()) {
            if (!winnerResult.containsKey(i)) {
                winnerResult.put(i, 0);
            }
        }
    }

    public Enum<PrizeStandards> getCorrespondingPrizeStandard(int score) {
        return PrizeStandards.getProperStandard(score);
    }

    public Map<Enum, Integer> returnWinnerResult() {
        return winnerResult;
    }
}
