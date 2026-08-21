package component_construction.storage.dynamic_loading;

import storage.external_interfaces.Loadable;

/**
 * Classes of this type must have a constructor that may be used to reflectively construct objects.
 * ReflectivelyLoadable
 */
public interface ReflectivelyLoadable extends Loadable {}