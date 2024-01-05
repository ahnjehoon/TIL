import entity.Blog;
import entity.Post;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        var blog = objectToJson(new Blog("첫번째 블로그", "주소.."), 0);
        var post = objectToJson(new Post(1, "첫번째 글", "제곧내", false), 0);

        System.out.println("converted blog");
        System.out.println(blog);

        System.out.println("converted post");
        System.out.println(post);
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
