package qaz.code.model;

import org.reactfx.value.Var;

public class Views {
    public final Var<Boolean> showMemoryPane = Var.newSimpleVar(true);
    public final Var<Boolean> showInputView = Var.newSimpleVar(true);
    public final Var<Boolean> showOutputView = Var.newSimpleVar(true);
    public final Var<Boolean> showLineOutput = Var.newSimpleVar(true);
    public final Var<Boolean> showOperationsView = Var.newSimpleVar(true);
}
