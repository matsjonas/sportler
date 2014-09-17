package util.views.tags;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class Gravatar {

    public static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
    public static final String GRAVATAR_URL_PARAMS = "d=mm";

    public static String render(final String email) {
        String md5Hex = DigestUtils.md5Hex(StringUtils.trimToEmpty(email).toLowerCase());
        return String.format("%s%s?%s", GRAVATAR_URL, md5Hex, GRAVATAR_URL_PARAMS);
    }

}
