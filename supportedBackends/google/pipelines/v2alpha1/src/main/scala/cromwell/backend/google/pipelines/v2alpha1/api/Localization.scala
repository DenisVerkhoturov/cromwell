package cromwell.backend.google.pipelines.v2alpha1.api

import com.google.api.services.genomics.v2alpha1.model.Mount
import cromwell.backend.google.pipelines.common.api.PipelinesApiRequestFactory.CreatePipelineParameters
import cromwell.backend.google.pipelines.v2alpha1.PipelinesConversions._

trait Localization {
  def localizeActions(createPipelineParameters: CreatePipelineParameters, mounts: List[Mount]) = {
    createPipelineParameters.inputOutputParameters.fileInputParameters.map(_.toAction(mounts, createPipelineParameters.projectId))
  }
}
