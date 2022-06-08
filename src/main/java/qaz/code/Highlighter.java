package qaz.code;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Highlighter {
    public static Pattern pattern = Pattern.compile(
            "(?<FIELD>[-+])|(?<POINTER>[><])|(?<IO>[,.])|(?<LOOP>[]\\[])|(?<COMMENT>.)"
    );

    private Executor executor = Executors.newSingleThreadExecutor();

    public Highlighter() {

    }
}
