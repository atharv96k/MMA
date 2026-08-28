package in.master.moneymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import in.master.moneymanager.dto.ExpenseDTO;
import in.master.moneymanager.dto.IncomeDTO;
import in.master.moneymanager.dto.RecentTransactionDTO;
import in.master.moneymanager.entity.ProfileEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Long profileId = profile.getId();

        Map<String, Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomesForProfile(profileId);
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForProfile(profileId);
        List<RecentTransactionDTO> recentTransactions = concat(latestIncomes.stream().map(income ->
                        RecentTransactionDTO.builder()
                                .id(income.getId())
                                .profileId(profileId)
                                .icon(income.getIcon())
                                .name(income.getName())
                                .amount(income.getAmount())
                                .date(income.getDate())
                                .createdAt(income.getCreatedAt())
                                .updatedAt(income.getUpdatedAt())
                                .type("income")
                                .build()),
                latestExpenses.stream().map(expense ->
                        RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profileId)
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .updatedAt(expense.getUpdatedAt())
                                .type("expense")
                                .build()))
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());

        // Compute each total exactly once and reuse, instead of querying it
        // separately for totalBalance and again for totalIncome/totalExpense.
        BigDecimal totalIncome = incomeService.getTotalIncomeForProfile(profileId);
        BigDecimal totalExpense = expenseService.getTotalExpenseForProfile(profileId);

        returnValue.put("totalBalance", totalIncome.subtract(totalExpense));
        returnValue.put("totalIncome", totalIncome);
        returnValue.put("totalExpense", totalExpense);
        returnValue.put("recent5Expenses", latestExpenses);
        returnValue.put("recent5Incomes", latestIncomes);
        returnValue.put("recentTransactions", recentTransactions);
        return returnValue;
    }

}
