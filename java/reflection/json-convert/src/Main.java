import entity.Blog;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        var blog = new Blog("첫번째 글", "주소..");
        var json = objectToJson(blog);
        System.out.println(json);
    }

    private static String objectToJson(Object instance) throws IllegalAccessException {
        var fields = instance.getClass().getDeclaredFields();
        var sb = new StringBuilder();

        sb.append("{");

        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) {
                continue;
            }

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
        }

        sb.append("}");

        return sb.toString();
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }

    private static String formatPrimitiveValue(Field field, Object instance) throws IllegalAccessException {
        if (field.getType().equals(boolean.class)
                || field.getType().equals(int.class)
                || field.getType().equals(long.class)
        ) {
            return field.get(instance).toString();
        }

        throw new RuntimeException(String.format("Type : %s 은 지원되지 않습니다", field.getType().getName()));
    }
}
