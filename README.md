[![](https://jitpack.io/v/amir1376/kotlin-validator.svg)](https://jitpack.io/#amir1376/kotlin-validator)



# A validator for kotlin
validate your inputs or objects with power of the kotlin typesafe builders



## Usage
quick introduction
```kotlin
val result=email.validate("yourEmail@gmail.com")
if(result.isValid){
    println("provided email is valid ,user:${result.user},host:${result.host}")
}else{
    replayToUser(result.reason!!.translate())
}
```
or
```kotlin
val result=compositeRule<User>{
    User::email mustBe email
    User::password mustBe inRange(8..64)
}.validate(user)
if(!result.isValid){
    println(result[User::email]?.translate())
    println(result[User::password]?.translate())
}
```


# Setup
## Dependency
put this into your gradle script
```groovy
repositories {
    //...
    maven { url "https://jitpack.io" }
}
dependencies {
    //...
    //android usage
    implementation 'com.github.amir1376.kotlin-validator:android:$version'
    
    //core (jvm)
    implementation 'com.github.amir1376.kotlin-validator:core:$version'
}
```



## Android
in your app entry point initialize validator translation
```kotlin
ValidatedTranslation.initDefaultAndroidAdapter(context)
    //include default translations
    .applyDefaultTranslations()
    
```


## JVM

before any use of translation provide a TranslationAdapter
here is the default adapter
```kotlin
ValidatedTranslation.initWithDefault()
```


# Features

## Combining rules together
you can validate your input by multiple rules
### Here is an example
```kotlin
val result=(email or empty).validate(input)
```

## Validate nested objects
sometimes you want to validate a model
the library has support this too
for example you have the following models
```kotlin
    data class User(
        val login:String,
        val name:Name,
        val password:String,
        val confirmPassword:String,
        val gender:String
    )
    data class Name(
        val first:String,
        val last:String,
    )
    enum class Gender{
        Male,Female
    }
```
you can validate this with this rule
```kotlin
//inside a suspend function
val userValidation=compositeRule<User>{
    User::login mustBe (startWithEnglishCharacter and inRange(6..64)) 
    User::name mustBe compositeRule<Name>{
        Name::first mustBe notEmpty
        Name::last mustBe notEmpty
    }
    User::password mustBe (
            containsAtLeastLowerCase(1) and
            containsAtLeastUpperCase(1) and 
            (containsAtLeastNumber(1)) and 
            inRange(8..64)
            )
    User::confirmPassword mustBe sameAs(User::password)
    User::gender mustBe oneOf<Gender>()
}
//user input
val user:User/*retrieve user object*/
val result=userValidation.validate(user)
if(!result.isValid){
//    you can get reason for each property
    result[User::login] 
}
```
if you can see in the above code, user password has a complex rule ,
but you can extract it to a variable
and because these rules are stateless (they don't store any reference of input)
you can safely use this combination multiple times
```kotlin
val strongPassword = containsAtLeastLowerCase(1) and
        containsAtLeastUpperCase(1) and
        (containsAtLeastNumber(1)) and
        inRange(8..64)
            //.... then replace
        User::password mustBe strongPassword
```
# Customization
## Creating your own rules
Of course, you can create your own rules with ease
here is an example
```kotlin
val phone get() = rule<String>{ input->
    if(phonePattern.matchEntire(input)){
        thenValid()
    }else{
        because("your provided phone number is not valid")
    }
}
```
## Localization
if your app has support of multiple language
when building your rules ,you have to provide `Reason`
instead of raw string
```kotlin
object PhoneInvalidReason:SingleReason
val phone get() = rule<String>{ input->
    if(phonePattern.matchEntire(input)){
        thenValid()
    }else{
        because(PhoneInvalidReason)
    }
}
```
then you have to provide PhoneInvalidReason translation to the adapter
```kotlin
ValidatedTranslation.adapter.apply{
    //declare translation here
    //this is up to you that how you want to translate that message
    PhoneInvalidReason::class providedBy {
        "your provided phone number is not valid"
    }
}
```

otherwise, if you are not interested on default translation approach,
then you can create your own translation by implementing `ValidatedTranslationAdapter`



## Android support
at the moment ,we have separated android module 
that contains an Android translation adapter,
it has some useful extensions to provide translation
from string resources
```kotlin
Validator.android().apply{
    //declare translation here
    //this is up to you that how you want to translate that message
    PhoneInvalidReason::class providedByResource (R.string.my_validation_phone_invalid)
}
```

if you use this library for android, you can use default translation provided 
by the android module.


currently supported languages are 
* English (default)
* Persian


### Coroutines support
the `validate` method is a suspend function
accordingly, rules are all suspend functions too,
so you can have suspended calls on them
and because of that, you have to call `validate` only in a coroutine scope
but ,if your validation rule hasn't any suspend calls, it is 
totally safe (there is no switching context in the core artifact) to
wrap `validate` into runBlocking block (if you have to)
 

# Attention
this library is still under beta
so that may have bugs

## Contribution
you can consider a pull request,
if you see unexpected behaviors in the library
or write more common plugins

otherwise, if you have suggestions or have seen something weird out there (😁),
please submit an issue
 
### TODOS
* write tests
* write more plugins
