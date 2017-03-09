package mekfarm.common;

import mekfarm.items.BaseAnimalFilterItem;

/**
 * Created by CF on 2017-03-07.
 */
public interface IAnimalAgeFilterAcceptor {
    boolean acceptsFilter(BaseAnimalFilterItem item);
}
