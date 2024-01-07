import entity.Blog;
import entity.Post;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, MalformedURLException {
        var post1 = new Post(1, "첫번째 글", "제곧내", false);
        var blog = new Blog("첫번째 블로그", new URL("http://a.b.c"), post1);

        System.out.println(objectToJson(blog, 0));
    }

    private static String objectToJson(Object instance, int indentSize) throws IllegalAccessException {
        var fields = instance.getClass().getDeclaredFields();
        var sb = new StringBuilder();

        sb.append(indent(indentSize));
        sb.append("{");
        sb.append("\n");

        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) {
                continue;
            }

            sb.append(indent(indentSize + 1));
            sb.append(formatStringValue(field.getName()));
            sb.append(":");

            if (field.getType().equals(String.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field, instance));
            } else if (field.getType().equals(java.net.URL.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else {
                sb.append(objectToJson(field.get(instance), indentSize + 1));
            }

            if (i != fields.length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent(indentSize));
        sb.append("}");

        return sb.toString();
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }

    private static String formatPrimitiveValue(Field field, Object instance) throws IllegalAccessException {
        if (field.getType().equals(boolean.class) || field.getType().equals(int.class) || field.getType().equals(long.class)) {
            return field.get(instance).toString();
        }

        throw new RuntimeException(String.format("Type : %s 은 지원되지 않습니다", field.getType().getName()));
    }

    private static String indent(int indentSize) {
        return "\t".repeat(Math.max(0, indentSize));
    }
}
