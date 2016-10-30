
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class WebsiteInfo extends BaseBean {
    private String url;
    private int type;
    private String label;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{type:");
        builder.append(type);
        builder.append(", url:");
        builder.append(url);
        builder.append(", label:");
        builder.append(label);
        builder.append("}");
        return builder.toString();
    }

    /*
     * URL:www.site.com
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        if (!ContactUtil.isEmpty(url)) {
            sb.append(VCardConstants.PROPERTY_URL);
            switch (type) {
                case 1:
                    sb.append(";HOMEPAGE");
                    break;
                case 2:
                    sb.append(";BLOG");
                    break;
                case 3:
                    sb.append(";PROFILE");
                    break;
                case 4:
                    sb.append(";HOME");
                    break;
                case 5:
                    sb.append(";WORK");
                    break;
                case 6:
                    sb.append(";FTP");
                    break;
                case 0:
                    if (!ContactUtil.isEmpty(this.label))
                        sb.append(";X-").append(this.label);
                    break;
            }
            sb.append(":").append(url);
            sb.append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        if (ContactUtil.isEmpty(url))
            return 0;

        return url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WebsiteInfo)) {
            return false;
        }
        WebsiteInfo website = (WebsiteInfo) obj;
        return ((url != null ? url.equalsIgnoreCase(website.url)
                : (website.url == null)));
    }
}
