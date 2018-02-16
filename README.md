# <img src="src/docs/images/roxana-logo.png">

A [Spring](https://spring.io/projects) based project framework created to guarantee a standardized RESTful services responses.

Roxana provides an easy and organized way to create and keep your own **business exceptions**. It also offers support to **Constraint Validation** on Spring Rest projects.

Business exceptions and Constraint Validation are treated as **user's friendly messages**, always keeping the **rest responses standardized**. 
All user's friendly messages could be **internationalized following the i18n pattern.**.

## Motivation

REST is a popular technology nowadays, much of it is due to the ease of implementation and its flexibility. 

However, in a long-term usage of this technology, it is crucial to keep a standard of URI nomenclature, request and response Json objects, HTTP methods and HTTP responses codes, not forgetting to always keep the source code as clean and simple as possible (Rever este trecho). All theses points could be used to measure the quality of a REST API.

As a developer, I spent(?) a lot of effort to standardize my APIs and keep things like throws and treat(deal?) business exceptions and internationalization painless.

When working with Spring technologies for the first time, I saw the opportunity to solve these problems and create an open source framework to make my solutions reusable and shareable with developers all around the world.

## Installing

#### Add Roxana as dependency of your project:

For Maven users: 

```xml
<dependencies>
  <dependency>
    <groupId>br.com.rooting</groupId>
    <artifactId>roxana</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
</dependencies> 
```
**Obs:** Roxana is not in the maven central yet, so you have to **clone the project** and deploy it in you local maven repository, with the command **"mvn clean install"**. You must to use 3.3.9 or superior maven version to build Roxana.

#### Enable Roxana framework on your Spring rest project with @EnableRoxana annotation:

```java
@EnableRoxana
@SpringBootApplication
public class YourStringRestApplication {
  ...
}
```
## How to use it:

Create your own business exception with @BusinessException annotation:

```java
package br.com.roxana.example;
...

// Your can define the Http Response e internationalization key if you don't want the default ones.
@BusinessException
public class InsufficientFundsException extends Exception {

    // You can create different type of parameters,
    // by using @Parameter, @DateParameter or @CurrencyParameter.
  @CurrencyParameter
  private final BigDecimal funds;

  public YourBusinessException(BigDecimal funds) {
    this.funds = funds;
  }

}
```
#### Put the business exception in your i18n files, by add the folow line:

```properties
br.com.roxana.example.InsufficientFundsException = Saldo Insuficiente: {funds}.
```

**Obs:** The translation above it is in brazilian portuguese.

**Obs:** Roxana uses "messages" as default path for i18n file, as well as, Spring framework.

#### Throw your business exception in some point of your code and let Roxana handled it:

```java 
...

@ResponseStatus(code = OK)
@RequestMapping(method = POST, path = "/{id}/cars/")
// Roxana provide a wrapper object that all rest responses should use.
// This make the api more standardized.
public @ResponseBody Response<Message> buyACar(@PathVariable("id") Long idPerson, 
              @RequestBody Car car) throws InsufficientFundsException {

    // The code below is not in a service layer because it is for a demo purpose.

    BigDecimal funds = Service.getFunds(idPerson);
    if(funs.compareTo(new BigDecimal(car.getPrice())) < 0) {
      throw new InsufficientFundsException(funds);
    }

    ...

    // You can mapping user's messages with enumations too.
    Message sucessRegisterMessage = this.createMessage(REGISTER_SUCESS);
    return GenericResponseBuilder.buildWith(response, sucessRegisterMessage);
}

...
```

#### When the code above throw the InsufficientFundsException, the body of the rest response will be like this:

```json
{
  "messages": [
    {
      "severity": "ERROR",
      "key": "{br.com.roxana.example.InsufficientFundsException}",
      "parameters": [
       {
          "funds": 3000.00
       }
      ],
      "language": "pt-BR",
      "translation": "Saldo Insuficiente: R$ 3000,00."
    }
  ]
}
```

**Obs:** There is others models of response that you can configure. See more in the **Main settings** section.

#### Roxana framework has a lot more features than what it is exposed here, so, if you are interested, check the example application [Amazing Book Store](https://github.com/rooting-company/amazing-book-store) or the documentation guide for more details.

## Main settings

Roxana has some configurations that changes the application behavior. The configuration should be placed in the spring configuration yml or properties file.

### Properties:

- **roxana.business.response-strategy:** Defines how the user's message will be formated in the rest responses. There are four options that you can use:

  * **FULLY:** It basically returns the translated message and everything used to compose it, such as the language, the parameters and key. 
    
    This is very useful to debug how the messages are composed in development environment or when you wish that the consumer of the api make them own internationalization based on yours.

  ```json  
  {
    "messages": [
      {
        "severity": "ERROR",
        "key": "{br.com.roxana.example.InsufficientFundsException}",
        "parameters": [
        {
            "funds": 3000.00
          }
        ],
        "language": "pt-BR",
        "translation": "Saldo Insuficiente: R$ 3000,00."
      }
    ]
  }
  ```
  
  * **TRANSLATED:** This is the default option. Translate the messages and return as a simple phrase.
 
  ```json 
  {
    "messages": [
      {
        "severity": "ERROR",
        "translation": "Saldo Insuficiente: R$ 3000,00."
      }
    ]
  }
  ```
  
  * **UNCHANGED:** The simpler format, only the severity and the internationalization keys are returned.
  
  ```json 
  {
    "messages": [
      {
        "severity": "ERROR",
        "key": "{br.com.roxana.example.InsufficientFundsException}"
      }
    ]
  }
  ```

- **roxana.business.exception-handler.suppress-others-exceptions:** Defines if **no** BusinessException like Null Pointer Exception, will be suppressed by a Intern Error user's friendly message. 

  It is useful when you want to hide unexpected exceptions in production environment. In other hands, you can allow the developer to see the stacktrace more easily in development environment. The default value is **"true"**.

- **roxana.message-bundle.suppress-fails-translations:** Defines when the application fails to translate a message, it will raise an exception or assume the pattern **???{messageKey}???** as translation. 

  This is useful when you want to avoid to compromise operations in production environment just because the programmer forgot to add a simple key in the i18n files. In the development environment, the programmer will notice missing keys more easily if the application rises exceptions. The default value is **"true"**.

- **roxana.message-bundle.path:** Define a custom path to the i18n file. The default value is **"message"**, as well as, Spring framework.

- **roxana.message-bundle.locale:** Define a custom locale that will be used to find the correct i18n file to translate the messages. When not defined, Roxana will look to **the locale of the system**.

## Documentation

Roxana's demo project ["Amazing Book Store"](https://github.com/rooting-company/amazing-book-store).

In progress...

## Backlog priorities

In progress...

## Contributors

* [Bruno Costa](https://github.com/brunoLNCosta) - brunoluisncosta@gmail.com

## License

The Spring Framework is released under version 2.0 of the
[Apache License](http://www.apache.org/licenses/LICENSE-2.0).
