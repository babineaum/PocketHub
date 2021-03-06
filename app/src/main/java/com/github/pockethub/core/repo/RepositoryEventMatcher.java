/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pockethub.core.repo;

import android.text.TextUtils;

import com.alorma.github.sdk.bean.dto.response.GithubEvent;
import com.alorma.github.sdk.bean.dto.response.Repo;
import com.alorma.github.sdk.bean.dto.response.events.EventType;
import com.alorma.github.sdk.bean.dto.response.events.payload.ForkEventPayload;
import com.github.pockethub.util.ConvertUtils;
import com.google.gson.Gson;

/**
 * Helper to find a {@link RepositoryEventMatcher} to open for an event
 */
public class RepositoryEventMatcher {

    /**
     * Get {@link Repo} from event
     *
     * @param event
     * @return gist or null if event doesn't apply
     */
    public Repo getRepository(final GithubEvent event) {
        if (event == null)
            return null;

        if (event.payload == null)
            return null;

        EventType type = event.getType();
        if (EventType.ForkEvent.equals(type)) {
            Repo repository = ((ForkEventPayload)event.payload).forkee;
            // Verify repository has valid name and owner
            if (repository != null && !TextUtils.isEmpty(repository.name)
                    && repository.owner!= null
                    && !TextUtils.isEmpty(repository.owner.login))
                return repository;
        }

        if (EventType.CreateEvent.equals(type) || EventType.WatchEvent.equals(type)
                || EventType.PublicEvent.equals(type))
            return ConvertUtils.eventRepoToRepo(event.repo);

        return null;
    }
}
