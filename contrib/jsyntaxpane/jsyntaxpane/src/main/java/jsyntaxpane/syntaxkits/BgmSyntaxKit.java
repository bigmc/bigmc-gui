package jsyntaxpane.syntaxkits;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.lexers.BgmLexer;

public class BgmSyntaxKit extends DefaultSyntaxKit {

    public BgmSyntaxKit() {
        super(new BgmLexer());
    }
}
