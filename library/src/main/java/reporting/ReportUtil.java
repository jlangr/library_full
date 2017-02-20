package reporting;

public class ReportUtil {
    public enum StringOp {
        pad, under
    }

    public static String transform(String x, int count, int spacing, ReportUtil.StringOp op) {
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer1 = new StringBuffer();
        String pads;
        switch (op) {
            case under:
                int i = 0;
                for (; i < count; i++)
                    buffer.append('-');
                String ptext = buffer.toString();
                pads = "";
                buffer1.append(ptext);
                int l = count + spacing - ptext.length();
                for (int j = 0; j < l; j++) {
                    pads += " ";
                }
                buffer1.append(pads);
                break;
            case pad:
                pads = " ";
                l = spacing;
                while (l > 1) {
                    pads = pads + " ";
                    l = l - 1;
                }
                buffer.append(x);
                buffer.append(pads);
                buffer1.append(buffer.toString());
                break;
        }
        return buffer1.toString();
    }

}
