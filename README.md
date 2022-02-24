#A validator for kotlin
validate your inputs or objects with the power kotlin typesafe builders
## Usage
this is only validate a simple input 
```kotlin
//inside a suspend function
val input="yourBeautifulEmail@gmail.com"
val result=email.validate(input)
if(result.isValid){
    println("provided email is valid")
//    in case of email validation
    println("user is: "+result.user)
    println("domain of email is: "+result.domain)
}else{
//if validationResult is invalid
//it always provide reason
    val reason=result.reason!!
//tell the user why input is invalid
    replayToUser(reason.translate().joinToString(" "))
}
```
# Setup
## Dependency
put this code snippet into your gradle script
```groovy
repositories {
    mavenCentral()
//  add jitpack repository
    maven { url "https://jitpack.io" }
}
dependencies {
//    to use in android
    implementation 'com.github.amir1376.kotlin-validator:android:$version'
    
//    to use in jvm 
//    his is the core artifact with no translations
    implementation 'com.github.amir1376.kotlin-validator:core:$version'
}
```
##Android
in your app entry point initialize validator translation
```kotlin
ValidatedTranslation.initDefaultAndroidAdapter()
```
##JVM
before any use of translation provide a TranslationAdapter
here is the default adapter
```kotlin
ValidatedTranslation.initWithDefault()
```


# Dive deeper
## Combining rules together
you can validate your input by multiple rules
### Here is an example
```kotlin
val result=(email or empty).validate(input)
```

## Validate nested objects
of course sometimes you want to validate a model
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
    User::confirmPassword mustBe sameAss(User::password)
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
if you can see in the above code user password has a complex rule ,
but you can extract it to a variable
and because these rules are stateless (not store any reference of input)
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
when building your rules you have to provide [Reason]
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
Validator.adapter.apply{
    //declare translation here
    //this is up to you that how you want to translate that message
    PhoneInvalidReason::class providedBy {
        "your provided phone number is not valid"
    }
}
```

otherwise, if you are not interested on default translation approach
you can implement your own translation by implementing `ValidatorTranslationAdapter`



## Android support
at the moment we have separated android module that have
that contains an Android translation adapter
it has some useful extensions for provide translation
from string resources
```kotlin
Validator.android().apply{
    //declare translation here
    //this is up to you that how you want to translate that message
    PhoneInvalidReason::class providedByResource (R.string.my_validation_phone_invalid)
}
```

if you use this library for android you can use default translation provided 
by the android module
the currently supported languages are 
* English (which is the default one if)
* Persian


### kotlin coroutines support
the `validate` method is a suspend function
accordingly, rules are all suspend functions,
so you can have suspended calls on them
and because of that you have to call validation only coroutine scope
but if your validation rule hasn't any suspend calls it is 
totally safe (there is no switching context in the core artifact) to
call `validate` in runBlocking block (if you have to)
 

#Attention
this library is still under beta
so that may have bugs

##Contribution
you can consider a pull request
if you see unexpected behaviors in the library
or write more common plugins

otherwise, if you have suggestions or see something weird :D
feel free to submit an issue
 
###TODOS
* write tests
* write more plugins