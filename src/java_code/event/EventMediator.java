package java_code.event;

import java_code.event.events.ClearDeletedEvent;
import java_code.event.events.Event;
import java_code.isDeletable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


class IllegalTypeException extends RuntimeException {
    public IllegalTypeException(String msg) {
        super(msg);
    }
}

class Wrapper implements IEventHandler {
    private Object obj;
    private Method m;

    public Wrapper(Object obj, Method m) {
        this.obj = obj;
        this.m = m;
    }

    public boolean isDeleted() {
        return m == null;
    }

    public void handle(Event e) {
        try {
            if (obj instanceof isDeletable && ((isDeletable) obj).isDeleted()) {
                obj = null;
                m = null;
            } else
                m.invoke(obj, e);
        } catch (InvocationTargetException | IllegalAccessException ignore) {
        }
    }
}

public class EventMediator {
    private static final EventMediator inst = new EventMediator();

    public static EventMediator get() {
        return inst;
    }

    private EventMediator() {
        registerObject(hMap);
    }

    private final HandlerMap hMap = new HandlerMap();

    static class MethodMap extends HashMap<Class<? extends Event>, List<IEventHandler>> {
    }

    private static class HandlerMap {
        private final MethodMap highest = new MethodMap();
        private final MethodMap high = new MethodMap();
        private final MethodMap normal = new MethodMap();
        private final MethodMap low = new MethodMap();
        private final MethodMap lowest = new MethodMap();

        private HandlerMap() {
        }

        private void checkEmpty(Class<? extends Event> key, MethodMap map) {
            if (!map.containsKey(key)) {
                map.put(key, new LinkedList<>());
            }
        }

        private MethodMap getMapOfPriority(EventPriority priority) {
            switch (priority) {
                case HIGHEST -> {
                    return highest;
                }
                case HIGH -> {
                    return high;
                }
                case NORMAL -> {
                    return normal;
                }
                case LOW -> {
                    return low;
                }
                case LOWEST -> {
                    return lowest;
                }
            }
            return new MethodMap();
        }

        private void register(Class<? extends Event> ev, IEventHandler handler, EventPriority priority) {
            MethodMap map = getMapOfPriority(priority);

            Class<? extends Event> cur = ev;

            Class<?> temp;

            do {
                checkEmpty(cur, map);

                map.get(cur).add(handler);

                temp = cur.getSuperclass();
                cur = (Class<? extends Event>) temp;

            } while (!cur.equals(Event.class.getSuperclass()));
        }

        private List<IEventHandler> getAllHandlersInOrder(Class<? extends Event> key) {
            List<IEventHandler> res = new LinkedList<>();

            MethodMap map;

            for (EventPriority p : EventPriority.values()) {
                map = getMapOfPriority(p);
                if (map.containsKey(key)) res.addAll(map.get(key));
            }

            return res;
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        private void clearDeleted(ClearDeletedEvent e) {
            MethodMap map;
            List<IEventHandler> new_;
            for (EventPriority p : EventPriority.values()) {
                map = getMapOfPriority(p);
                for (Class<? extends Event> ev : map.keySet()) {
                    new_ = new LinkedList<>();
                    for (IEventHandler h : map.get(ev)) {
                        if (!h.isDeleted()) {
                            new_.add(h);
                        }
                    }
                    map.put(ev, new_);
                }
            }
            e.cancel();
        }
    }

    public void callEvent(Event ev) {
        Class<? extends Event> cls = ev.getClass();

        for (IEventHandler h : hMap.getAllHandlersInOrder(cls)) {
            h.handle(ev);
            if (ev.isCanceled()) {
                break;
            }
        }
    }

    private Class<? extends Event> getMethodEventAndCheck(Method m) {
        Class<?>[] types = m.getParameterTypes();
        if (types.length != 1) {
            throw new IllegalArgumentException("event handler must have one and only one argument");
        }

        Class<?> cls = types[0];

        if (!Event.class.isAssignableFrom(cls)) {
            throw new IllegalTypeException("event handler's argument's type should be a subtype of Event");
        }

        return (Class<? extends Event>) cls;
    }

    private EventPriority getEventPriorityOfMethod(Method m) {
        return m.getAnnotation(SubscribeEvent.class).priority();
    }

    private void registerMethod(Object obj, Method m) {
        IEventHandler h = new Wrapper(obj, m);
        EventPriority p = getEventPriorityOfMethod(m);
        Class<? extends Event> key = getMethodEventAndCheck(m);

        hMap.register(key, h, p);
    }

    public void registerClass(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.isAnnotationPresent(SubscribeEvent.class))
                .forEach(m -> {
                    m.setAccessible(true);
                    registerMethod(null, m);
                });
    }

    private Class<?>[] getClasses(Object obj) {
        List<Class<?>> lst = new LinkedList<>();

        Class<?> cur = obj.getClass();

        do {
            lst.add(cur);
            cur = cur.getSuperclass();
        } while (cur != null);

        Class<?>[] res = new Class[lst.size()];

        int i = 0;

        for (Class<?> cls : lst) {
            res[i] = cls;
            i++;
        }

        return res;
    }

    public void registerObject(Object obj) {
        Arrays.stream(getClasses(obj))
                .forEach(c ->
                        Arrays.stream(c.getDeclaredMethods())
                                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                                .filter(m -> m.isAnnotationPresent(SubscribeEvent.class))
                                .forEach(m -> {
                                    m.setAccessible(true);
                                    registerMethod(obj, m);
                                })
                );
    }
}
