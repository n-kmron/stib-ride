package repository.dto;

import java.util.Objects;

/**
 * Data transfer object
 * @param <K> the type of the key for the object
 */
public abstract class Dto<K> {

        protected K key;

        public abstract K getKey();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dto<?> dto = (Dto<?>) o;
            return Objects.equals(key, dto.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
}

