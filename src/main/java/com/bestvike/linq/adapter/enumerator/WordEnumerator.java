package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class WordEnumerator extends AbstractEnumerator<String> {
    private final String source;

    public WordEnumerator(String source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        if (this.state < 0)
            return false;

        int length = this.source.length();
        while (this.state < length) {
            char head = this.source.charAt(this.state);
            if (!Character.isLetterOrDigit(head)) {
                this.state++;
                continue;
            }
            int beginIndex = this.state++;
            while (this.state < length) {
                char tail = this.source.charAt(this.state);
                if (!Character.isLetterOrDigit(tail))
                    break;
                this.state++;
            }
            this.current = this.source.substring(beginIndex, this.state);
            return true;
        }

        this.close();
        return false;
    }
}