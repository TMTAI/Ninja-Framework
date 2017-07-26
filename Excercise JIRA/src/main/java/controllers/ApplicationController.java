/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import ninja.Result;
import ninja.Results;

import com.google.inject.Singleton;
import ninja.session.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import service.DatabaseUtility;
import util.Constant;

import static service.UserService.getJiraProjectofUserfromServer;
import static service.UserService.getUserInformation;
import static util.Constant.*;


@Singleton
public class ApplicationController extends DatabaseUtility {

    public Result index(Session session) {
        if (session.isEmpty()){
            return Results.redirect("/login");
        }
        return Results.html();
    }

    public Result getUserInfo(Session session) {
        try {
            JSONObject info = getUserInformation(session);
            JSONObject userInfo = new JSONObject();
            userInfo.put(DISPLAY_NAME, info.getString(Constant.ALIAS));
            userInfo.put(Constant.GROUPS, new JSONArray(info.getString(USER_GROUPS)));
            userInfo.put(ADMIN, info.getBoolean(ADMIN));
            userInfo.put(Constant.NAME, session.get(USERNAME));
            userInfo.put(USER_PROJECTS, getJiraProjectofUserfromServer(session));
            return Results.text().render(userInfo);
        } catch (Exception e) {
            return Results.internalServerError();
        }

    }

    public Result getProjectList(Session session) {
        try {
            return Results.text().render(getJiraProjectofUserfromServer(session));
        } catch (Exception e) {
            return Results.internalServerError();
        }

    }
}