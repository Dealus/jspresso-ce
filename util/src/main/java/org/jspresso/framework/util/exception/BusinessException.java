/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.exception;

import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;


/**
 * A "normal" business exception. Whenever a contextual translated message is
 * needed, the method "getI18nMessage" might be overriden.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class BusinessException extends RuntimeException {

  private static final long serialVersionUID = -5422600831610337684L;

  private String staticI18nKey;

  /**
   * Constructs a new <code>BusinessException</code> instance.
   * 
   * @param message
   *            the exception message.
   */
  public BusinessException(String message) {
    super(message);
  }

  /**
   * Constructs a new <code>BusinessException</code> instance.
   * 
   * @param message
   *            the exception message.
   * @param staticI18nKey
   *            the static i18n key if any. It will be used by default to get
   *            the internationalized message.
   */
  public BusinessException(String message, String staticI18nKey) {
    super(message);
    this.staticI18nKey = staticI18nKey;
  }

  /**
   * Gets the exception localized message using a translation provider.
   * 
   * @param translationProvider
   *            the translation provider used to translate the exception
   *            message.
   * @param locale
   *            the locale to translate the exception to.
   * @return the translated message.
   */
  public String getI18nMessage(ITranslationProvider translationProvider,
      Locale locale) {
    return translationProvider.getTranslation(staticI18nKey, locale);
  }

}
