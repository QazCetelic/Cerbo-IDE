package qaz.code.view;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Highlighter {
    public static final Pattern PATTERN = Pattern.compile(
            "(?<FIELD>[-+])|(?<POINTER>[><])|(?<IO>[,.])|(?<LOOP>[]\\[])|(?<COMMENT>.)"
    );

    private Executor executor = Executors.newSingleThreadExecutor();

    public Highlighter() {

    }
}
