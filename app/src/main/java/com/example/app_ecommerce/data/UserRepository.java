package com.example.app_ecommerce.data;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutionException;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {
        UserDatabase db = UserDatabase.getInstance(application);
        userDao = db.getContactDAO();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public User login(String mobile, String password) throws ExecutionException, InterruptedException {
        return new LoginUserAsyncTask(userDao).execute(mobile, password).get();
    }

    public User getUserById(int userId) throws ExecutionException, InterruptedException {
        return new GetUserByIdAsyncTask(userDao).execute(userId).get();
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class LoginUserAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        private LoginUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return userDao.login(strings[0], strings[1]);
        }
    }
    public User getUserByMobile(String mobile) throws ExecutionException, InterruptedException {
        return new GetUserByMobileAsyncTask(userDao).execute(mobile).get();
    }

    private static class GetUserByMobileAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        private GetUserByMobileAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... mobiles) {
            return userDao.getUserByMobile(mobiles[0]);
        }
    }
    private static class GetUserByIdAsyncTask extends AsyncTask<Integer, Void, User> {
        private UserDao userDao;

        private GetUserByIdAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(Integer... integers) {
            return userDao.getUserById(integers[0]);
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }
}
