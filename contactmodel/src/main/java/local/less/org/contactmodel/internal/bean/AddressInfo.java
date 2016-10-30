
package local.less.org.contactmodel.internal.bean;


import local.less.org.contactmodel.internal.ContactUtil;
import local.less.org.contactmodel.internal.vcard.VCardConfig;
import local.less.org.contactmodel.internal.vcard.VCardConstants;

public class AddressInfo extends BaseBean {
    public static final int ADDR_MAX_DATA_SIZE = 7;

    private int type;
    private int isPrimary;
    private String country;
    private String region;
    private String city;
    private String street;
    private String postCode;
    private String pobox;
    private String extendedAddress;
    private String label;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(int isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPobox() {
        return pobox;
    }

    public String getExtendedAddress() {
        return extendedAddress;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPobox(String pobox) {
        this.pobox = pobox;
    }

    public void setExtendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{country:");
        builder.append(country);
        builder.append(", region:");
        builder.append(region);
        builder.append(", city:");
        builder.append(city);
        builder.append(", street:");
        builder.append(street);
        builder.append(", type:");
        builder.append(type);
        builder.append(", postCode:");
        builder.append(postCode);
        builder.append(", pobox:");
        builder.append(pobox);
        builder.append(", extendedAddress:");
        builder.append(extendedAddress);
        builder.append(", label:");
        builder.append(label);
        builder.append(", isPrimary:");
        builder.append(isPrimary);
        builder.append("}");
        return builder.toString();
    }

    public String getFormattedAddress(final int vcardType) {
        String[] dataArray = new String[ADDR_MAX_DATA_SIZE];
        dataArray[0] = this.pobox;
        dataArray[1] = this.extendedAddress;
        dataArray[2] = this.street;
        dataArray[3] = this.city;
        dataArray[4] = this.region;
        dataArray[5] = this.postCode;
        dataArray[6] = this.country;

        StringBuilder builder = new StringBuilder();
        boolean empty = true;
        if (VCardConfig.isJapaneseDevice(vcardType)) {
            // In Japan, the order is reversed.
            for (int i = ADDR_MAX_DATA_SIZE - 1; i >= 0; i--) {
                String addressPart = dataArray[i];
                if (!ContactUtil.isEmpty(addressPart)) {
                    if (!empty) {
                        builder.append(' ');
                    } else {
                        empty = false;
                    }
                    builder.append(addressPart);
                }
            }
        } else {
            for (int i = 0; i < ADDR_MAX_DATA_SIZE; i++) {
                String addressPart = dataArray[i];
                if (!ContactUtil.isEmpty(addressPart)) {
                    if (!empty) {
                        builder.append(' ');
                    } else {
                        empty = false;
                    }
                    builder.append(addressPart);
                }
            }
        }

        return builder.toString().trim();
    }

    /*
     * 格式：ADR;WORK;CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:;;=E5=85=B6=E4=BB=96=E5
     * =
     * 9C=B0=E6=96=B9=20;=E4=B8=9C=E5=8C=97;=E4=B8=AD=E5=8D=97=2C=20=E5=8C=97=E4
     * =BA=AC=31;=38=39=38=38=38=37; 地址组成：the post office box; the extended
     * address; the street address; the locality (e.g.,city); the region
     * (e.g.,state or province); the postal code; the country name
     */
    public String toVCardString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VCardConstants.PROPERTY_ADR);
        switch (type) {
            case 1:
                sb.append(";HOME");
                break;
            case 2:
                sb.append(";WORK");
                break;
            case 0:
                if (!ContactUtil.isEmpty(this.label))
                    sb.append(";X-").append(this.label);
                break;
        }
        sb.append(";CHARSET=UTF-8;ENCODING=QUOTED-PRINTABLE:");
        if (!ContactUtil.isEmpty(pobox))
            sb.append(ContactUtil.qpEncoding(pobox));
        sb.append(";");
        if (!ContactUtil.isEmpty(extendedAddress))
            sb.append(ContactUtil.qpEncoding(extendedAddress));
        sb.append(";");
        if (!ContactUtil.isEmpty(street))
            sb.append(ContactUtil.qpEncoding(street));
        sb.append(";");
        if (!ContactUtil.isEmpty(city))
            sb.append(ContactUtil.qpEncoding(city));
        sb.append(";");
        if (!ContactUtil.isEmpty(region))
            sb.append(ContactUtil.qpEncoding(this.region));
        sb.append(";");
        if (!ContactUtil.isEmpty(postCode))
            sb.append(ContactUtil.qpEncoding(this.postCode));
        sb.append(";");
        if (!ContactUtil.isEmpty(country))
            sb.append(ContactUtil.qpEncoding(this.country));
        sb.append("\r\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        String formatAddress = getFormattedAddress(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8);
        if (ContactUtil.isEmpty(formatAddress))
            return 0;
        return formatAddress.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AddressInfo)) {
            return false;
        }
        final AddressInfo postalData = (AddressInfo) obj;
        String address1 = getFormattedAddress(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8);
        String address2 = postalData
                .getFormattedAddress(VCardConfig.VCARD_TYPE_V21_JAPANESE_UTF8);
        return ((address1 != null ? address1.equalsIgnoreCase(address2)
                : (address2 == null)));
    }
}
