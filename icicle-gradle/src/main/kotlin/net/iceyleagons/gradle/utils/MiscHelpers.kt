package net.iceyleagons.gradle.utils

import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.tasks.TaskContainer

operator fun ConfigurationContainer.get(name: String): Configuration = getByName(name)
operator fun TaskContainer.get(name: String): Task = named(name).orNull!!