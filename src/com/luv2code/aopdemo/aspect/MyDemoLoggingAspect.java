package com.luv2code.aopdemo.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.aopdemo.Account;

@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspect {
	// NEXT UP:@After
	// s'execute toujours après la méthode (qu'elle génère une exception ou pas),
	// mais avant @AfterThrowing (exactement comme un finally s'execute avant
	// l'exception dans une fonction java). Cela dit, les nlles version de Spring
	// executent @After avant @AfterThrowing si les deux sont dans le même advice et
	// utilisent le même join point

	// @After sert surtout à executer un code
	// dans tout les cas.

	// On créer donc un advice à faire tourner après la méthode (finally ...
	// success/échec). On écrit comme suit:
	// @After("execution(justelecheminjzofjeoz)")
	// public void afterFinallyFindAccountsAdvice() {
	// System.out.println("Executing @After (finally) advice")
	// }

	// bien à savoir:
	// @After n'a pas accès aux exceptions,s'il nous fait l'exception
	//// alors @AfterThrowing est plus approprié
	// l'advice @After devrait être executable dans tt les cas, erreur ou pas
	// l'usage le plus approprié pour @After c'est les vérifications et le loggings
	// 1. préparer nlle applis
	// 2.ajouter un advice @After
	// 3.vérifier le cas où il n'y a pas d'exception (du coup on modifie aussi le
	// try-catch bloc qui nous génèrait une exception)

	@After("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))")
	public void afterFinallyFindAccountsAdvice(JoinPoint theJoinPoint) {
		// imprimer methode de cet advice
		String method = theJoinPoint.getSignature().toShortString();
		System.out.println("\n---------------> Executing @After on method: " + method);
	}

	@AfterThrowing(pointcut = "execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", throwing = "theExc")
	public void afterThrowingFindAccountsAdvice(JoinPoint theJoinPoint, Throwable theExc) {
		// imprimer methode de cet advice
		String method = theJoinPoint.getSignature().toShortString();
		System.out.println("\n---------------> Executing @AfterThrowing on method: " + method);
		// logger l'exception
		System.out.println("\n---------------> result is: " + theExc);
	}

	@AfterReturning(pointcut = "execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", returning = "result")
	public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {
		// imprimer le nom de la methode sur laquelle on aura le advice
		String method = theJoinPoint.getSignature().toShortString();
		System.out.println("\n---------------> Executing @AfterReturning on method: " + method);
		// imprimer les resultats de la methode
		System.out.println("\n---------------> result is: " + result);
		// modifier les données
		// convertir les noms des Account en MAJUSCULES
		convertAccountNamesToUppercase(result);
		System.out.println("\n---------------> result is: " + result);
	}

	private void convertAccountNamesToUppercase(List<Account> result) {
		// boucler sur les accounts
		for (Account tempAccount : result) {
			// choper version en majuscules
			String theUpperName = tempAccount.getName().toUpperCase();
			// mettre à jour le nom de l'account
			tempAccount.setName(theUpperName);
		}
	}

	@Before("com.luv2code.aopdemo.aspect.LuvAopExpressions.forDaoPackageNoGetterSetter()")
	public void beforeAddAccountAdvice(JoinPoint theJoinPoint) {
		System.out.println("\n----------> Executing @Before advice on addAccount()");
		// afficher la signature de la methode
		MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
		System.out.println("Method: " + methodSig);
		// afficher les args de la methode, pour ce faire:
		//// 1. choper les args
		Object[] args = theJoinPoint.getArgs();
		//// 2. boucler sur les args
		for (Object tempArg : args) {
			System.out.println(tempArg);

			if (tempArg instanceof Account) {
				Account theAccount = (Account) tempArg;
				System.out.println("account name: " + theAccount.getName());
				System.out.println("account level: " + theAccount.getLevel());

			}

		}
	}

}
