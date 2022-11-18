package qaz.code.model

import org.reactfx.value.Var

class Views {
    @JvmField
    val showMemoryPane: Var<Boolean> = Var.newSimpleVar(true)
    @JvmField
    val showInputView: Var<Boolean> = Var.newSimpleVar(true)
    @JvmField
    val showOutputView: Var<Boolean> = Var.newSimpleVar(true)
    @JvmField
    val showLineOutput: Var<Boolean> = Var.newSimpleVar(true)
    @JvmField
    val showOperationsView: Var<Boolean> = Var.newSimpleVar(true)
}