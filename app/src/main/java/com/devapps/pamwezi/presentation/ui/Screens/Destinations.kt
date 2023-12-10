package com.devapps.pamwezi.presentation.ui.Screens

interface Destinations {
    val route: String
}

object Home : Destinations {
    override val route: String = "home_screen"
}

object AddBudget : Destinations {
    override val route: String = "add_budget"
}

object AddExpense : Destinations {
    override val route: String = "add_expense"
}

object BudgetExpenses : Destinations {
    override val route: String = "budget_expenses"

}

object SignOutUser : Destinations {
    override val route: String = "logout_user"
}

