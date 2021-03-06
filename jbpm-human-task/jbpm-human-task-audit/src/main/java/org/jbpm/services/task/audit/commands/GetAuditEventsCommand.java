/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.jbpm.services.task.audit.commands;

import org.jbpm.services.task.commands.TaskCommand;
import org.jbpm.services.task.utils.ClassUtil;
import org.kie.api.runtime.Context;
import org.kie.internal.query.QueryFilter;
import org.kie.internal.task.api.TaskContext;
import org.kie.internal.task.api.TaskPersistenceContext;
import org.kie.internal.task.api.model.TaskEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="get-audit-events-for-task-command")
@XmlAccessorType(XmlAccessType.NONE)
public class GetAuditEventsCommand extends TaskCommand<List<TaskEvent>> {

	private static final long serialVersionUID = -7929370526623674312L;
        private QueryFilter filter;
	public GetAuditEventsCommand() {
            this.filter = new QueryFilter(0,10);
	}
	
	public GetAuditEventsCommand(long taskId, QueryFilter filter) {
		this.taskId = taskId;
                this.filter = filter;
	}
       
	@Override
	public List<TaskEvent> execute(Context context) {
		TaskPersistenceContext persistenceContext = ((TaskContext) context).getPersistenceContext();
		if( this.taskId != null ) { 
		    return persistenceContext.queryWithParametersInTransaction("getAllTasksEvents", 
		            persistenceContext.addParametersToMap("taskId", taskId, "firstResult", filter.getOffset(), 
                                    "maxResults", filter.getCount()),
		            ClassUtil.<List<TaskEvent>>castClass(List.class));
		} else { 
		    return persistenceContext.queryStringWithParametersInTransaction("FROM TaskEventImpl",persistenceContext.addParametersToMap("firstResult", filter.getOffset(),
                                                                                                "maxResults", filter.getCount()),
		            ClassUtil.<List<TaskEvent>>castClass(List.class));
		}
	}

}
