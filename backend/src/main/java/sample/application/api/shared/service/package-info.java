/// Base classes for implementing services to be used by REST controllers.
///
/// Create a service class for each [sample.application.api.shared.model.AbstractBaseModel] class in the system.
/// Such a class should be created in the sub-package within [sample.application.api.feature],
/// containing the classes for implementing a specific system functionality
/// (such as service, repository, and controller classes for a given registration).
/// Without the specific service class, you may encounter an error
/// "AbstractRestController required a bean of type 'CrudService' that could not be found",
/// as no class extending `CrudService<T extends AbstractEntity>` will be found.
///
package sample.application.api.shared.service;

