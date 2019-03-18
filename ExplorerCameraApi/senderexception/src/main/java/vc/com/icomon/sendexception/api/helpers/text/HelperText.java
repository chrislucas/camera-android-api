package vc.com.icomon.sendexception.api.helpers.text;

public class HelperText {
    public static String removeAccents(String target) {
        String accents = "ÀÁÂÃÄÅàáâãäåÒÓÔÕÕÖØòóôõöøÈÉÊËèéêëðÇçÐÌÍÎÏìíîïÙÚÛÜùúûüÑñŠšŸÿýŽž";
        String accentsOut = "AAAAAAaaaaaaOOOOOOOooooooEEEEeeeeeCcDIIIIiiiiUUUUuuuuNnSsYyyZz";
        for (int i = 0; i < target.length() ; i++) {
            char ch = target.charAt(i);
            String c = String.valueOf(ch);
            if (accents.contains(c)) {
                int idx = accents.indexOf(ch);
                String n = String.valueOf(accentsOut.charAt(idx));
                target = target.replace(c, n);
            }
        }
        return target;
    }
}
