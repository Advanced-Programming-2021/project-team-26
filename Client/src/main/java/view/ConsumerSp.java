package view;

@FunctionalInterface
public interface ConsumerSp<T> {
    String accept(T t);
}
